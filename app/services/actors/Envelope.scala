package services.actors

/**
 * Envelope for sending payload to a entity with a UUID.
 *  * @param id The receiver of the envelope.
 * @param payload The payload in this envelope.
 * @tparam I type of id
 * @tparam P type of content
 * Created by peixiaobin on 2020/11/25.
 */
case class Envelope[I, P](id: I, payload: P)
